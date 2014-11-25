package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import anorm._

import models._
import views._

object Ideas extends Controller {

  val form = Form(
    mapping(
      "id" -> ignored(None:Option[Long]),
      "title" -> nonEmptyText,
      "content" -> optional(text))(Idea.apply)(Idea.unapply))
  
  def list = Action { implicit request =>
    Ok(html.ideas.list(Idea.list))
  }

  def add = Action {
    Ok(html.ideas.add(form))
  }

  def save = Action { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => BadRequest(html.ideas.add(formWithErrors)),
      idea => {
        Idea.insert(idea)
        Redirect(routes.Ideas.list).flashing("success" -> "Idea %s has been created".format(idea.title))
      }
      
    )
  }

  def edit(id: Long) = Action {
    Idea.findById(id).map { idea =>
      Ok(html.ideas.edit(id, form.fill(idea)))
    }.getOrElse(NotFound)
  }

  def update(id: Long) = Action { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => BadRequest(html.ideas.edit(id, formWithErrors)),
      idea => {
        Idea.update(id, idea)
        Redirect(routes.Ideas.list).flashing("success" -> "Idea %s has been updated".format(idea.title))
      }
    )
  }

  def delete(id: Long) = Action {
    Idea.delete(id)
    Redirect(routes.Ideas.list).flashing("success" -> "Idea has been deleted")
  }
}
