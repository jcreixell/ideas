package models

import anorm._ 
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

case class Idea(id: Option[Long] = None, title: String, content: Option[String]) {
  override def toString = s"$title -> $content"
}

object Idea {
  private val ideaParser: RowParser[Idea] = {
    get[Option[Long]]("id") ~
    get[String]("title") ~
    get[Option[String]]("content") map {
      case id ~ title ~ content => Idea(id, title, content)    
    }

  }

  def insert(idea: Idea) = {
    DB.withConnection { implicit connection =>
      SQL("""
            INSERT INTO ideas(id, title, content)
            VALUES ((SELECT NEXT VALUE FOR idea_id_seq), {title}, {content})
      """).on(
        'title -> idea.title,
        'content -> idea.content
      ).executeUpdate()
    }
  }

  def list: List[Idea] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT * FROM ideas").as(ideaParser *)
    }
  }

  def findById(id: Long): Option[Idea] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT * FROM ideas WHERE id = {id}")
        .on('id -> id)
        .as(ideaParser.singleOpt)
    }
  }

  def update(id: Long, idea: Idea) = {
    DB.withConnection { implicit connection =>
      SQL("""UPDATE ideas SET
             title = {title},
             content = {content}
             WHERE id = {id}
          """).on(
            'id -> id,
            'title -> idea.title,
            'content -> idea.content
          ).executeUpdate
    }
  }

  def delete(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL"DELETE FROM ideas where id = $id".executeUpdate
    } 
  }
}
