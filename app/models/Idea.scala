package models

case class Idea(title: String, content: String) {
  var id: Int = Idea.nextId
}

object Idea {
  private var currentId = 0

  def nextId: Int = {
    currentId += 1
    currentId
  }
}
