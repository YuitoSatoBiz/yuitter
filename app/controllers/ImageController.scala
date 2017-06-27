package controllers

import java.nio.file.FileSystems
import javax.inject.Inject

import play.api.Logger
import play.api.libs.Files
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller, MultipartFormData}
import security.AuthenticatedAction

import scala.concurrent.ExecutionContext

/**
  * Created by satoyuito on 2017/06/27.
  */
class ImageController @Inject()(implicit val ec: ExecutionContext) extends Controller {

  def upload: Action[MultipartFormData[Files.TemporaryFile]] = Action(parse.multipartFormData) { implicit rs =>
    rs.body.file("image").map { image =>
      import java.io.File
      val filename = image.filename
      val projectPath = FileSystems.getDefault.getPath("public/images/").toAbsolutePath.toString
      image.ref.moveTo(new File(s"$projectPath/$filename"))
      Ok(Json.obj("result" -> "success"))
    }.getOrElse {
      BadRequest(Json.obj("result" -> "failure"))
    }
  }
}
