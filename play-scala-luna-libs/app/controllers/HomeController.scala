package controllers

import javax.inject._

import play.api.mvc._
import play.api.mvc.Cookie

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import java.util.Collections

//import play.db._
import play.api.db._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(db: Database, cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(s : String) = Action {
    Ok(views.html.index(s))
  }

  def authenticate() = Action { request =>
    play.api.Logger.info("received authentication request");
    val httpTransport = new NetHttpTransport()
    val jsonFactory = new JacksonFactory()
    var verifier = new GoogleIdTokenVerifier.Builder( httpTransport , jsonFactory )
      // Specify the CLIENT_ID of the app that accesses the backend:
      .setAudience(Collections.singletonList("772491297294-cv13bbftqun0hkmgsnqldf0agnaf9ev9.apps.googleusercontent.com"))
      // Or, if multiple clients access the backend:
      //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
      .build();

    // (Receive idTokenString by HTTPS POST)
    //play.api.Logger.info("query length: " + request.queryString.entrie)
    //val idTokenString = request.queryString.get("idtoken").flatMap(_.headOption).toString
    //val idTokenString2 = request.body
    var idTokenString = "None"
    val idTokenString2 = request.body.asFormUrlEncoded.get("idtoken")
    play.api.Logger.info("id token string: " + idTokenString)


    //play.api.Logger.info("queryString: " + idTokenString2.asFormUrlEncoded.get("idtoken"))
    for (i <- idTokenString2) {
      play.api.Logger.info("queryString: " + i)
      idTokenString = i
    }

    if (idTokenString != null && idTokenString != "" && idTokenString != "None") {
      try {
        val idToken: GoogleIdToken = verifier.verify(idTokenString);
        play.api.Logger.info("id token: " + idToken);
        val payload = idToken.getPayload();

        // Print user identifier
        val userId = payload.getSubject();
        play.api.Logger.info("User ID: " + userId);

        // Get profile information from payload
        val id_sub = payload.get("sub");
        val email = payload.getEmail();
        val domain = payload.get("hd");
        val name = payload.get("name");
        val pictureUrl = payload.get("picture");
        play.api.Logger.debug("-----------------------------------------");
        play.api.Logger.debug("-------------USER INFORMATON-------------");
        play.api.Logger.debug("-----------------------------------------");
        play.api.Logger.info("User ID_sub: " + id_sub);
        play.api.Logger.info("User name: " + name);
        play.api.Logger.info("User email: " + email);
        play.api.Logger.info("User domain: " + domain);
        play.api.Logger.info("User picture url: " + pictureUrl);
        play.api.Logger.debug("-----------------------------------------");
        //val locale = payload.get("locale");
        //val familyName = payload.get("family_name");
        //val givenName = payload.get("given_name");
        // Use or store profile information
        db.withConnection { connection =>
          play.api.Logger.debug("-------------DB CONNECTION START----------------")
          var q = "SELECT * FROM libraries"
          val stmt = connection.createStatement()
          val rs = stmt.executeQuery(q)
          while (rs.next()) {
            play.api.Logger.debug(rs.getString("name"))
          }
          play.api.Logger.debug("-------------DB CONNECTION END------------------")
        }
      }
    }
    Ok("performed task")
  }

}
