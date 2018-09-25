package controllers2
import javax.inject.Inject

import play.api.db._
import play.api.mvc._

class ScalaControllerInject @Inject()(db: Database, val controllerComponents: ControllerComponents) extends BaseController {

  def index = Action {
    var outString = ""
    val conn = db.getConnection()

    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT * as testkey")

      while (rs.next()) {
        outString += rs.getString("testkey")
      }
    } finally {
      conn.close()
    }
    play.api.Logger.debug("DB THINGS: " + outString);
    Ok(outString)
  }

}