package controllers

import javax.inject._

import play.api.mvc._
import play.api.mvc.Cookie

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
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

  def authenticate() = Action {
    import java.util.Collections
    val verifier = new Nothing(transport, jsonFactory).setAudience // Specify the CLIENT_ID of the app that accesses the backend:
    Collections.singletonList(CLIENT_ID).build // Or, if multiple clients access the backend:
    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))


    // (Receive idTokenString by HTTPS POST)

    val idToken = verifier.verify(idTokenString)
    if (idToken != null) {
      val payload = idToken.getPayload
      // Print user identifier
      val userId = payload.getSubject
      println("User ID: " + userId)
      // Get profile information from payload
      val email = payload.getEmail
      val emailVerified = Boolean.valueOf(payload.getEmailVerified)
      val name = payload.get("name").asInstanceOf[String]
      val pictureUrl = payload.get("picture").asInstanceOf[String]
      val locale = payload.get("locale").asInstanceOf[String]
      val familyName = payload.get("family_name").asInstanceOf[String]
      val givenName = payload.get("given_name").asInstanceOf[String]
      // Use or store profile information
      // ...
    }
    else println("Invalid ID token.")
  }

}
