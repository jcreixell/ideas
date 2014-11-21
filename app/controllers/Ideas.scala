package controllers

import play.api.mvc._
import scala.collection.mutable.HashMap
import play.api.data._
import play.api.data.Forms._
import models.Idea

object Ideas extends Controller {

  val ideas: HashMap[Int, Idea] = new HashMap[Int, Idea]

  val form = Form(
    mapping(
      "title" -> text,
      "content" -> text)(Idea.apply)(Idea.unapply))
  
  def list = Action {
    Ok(views.html.ideas.list(ideas.values))
  }

  def add = Action {
    Ok(views.html.ideas.add(form))
  }

  def save = Action { implicit request =>
      val idea = form.bindFromRequest.get
      ideas.put(idea.id, idea)
      Redirect(routes.Ideas.list)
  }

  def edit(id: Int) = Action {
    val bindedForm = form.fill(ideas.get(id).get)
    Ok(views.html.ideas.edit(bindedForm))
  }

  def update(id: Int) = Action { implicit request =>
    val idea = form.bindFromRequest.get
    idea.id = id
    ideas.put(id, idea)
    Redirect(routes.Ideas.list)
  }

  def delete(id: Int) = Action {
    ideas.remove(id)
    Redirect(routes.Ideas.list)
  }
}
