package db_classes

case class office (office_id : Int, name : String, img_url : String, images_url : String, location : String, books : String, phone_number : String, mail_address : String)
case class library (library_id : Int, admin : Boolean, name : String, img_url : String, location : String, books : String, wanted : String, phone_number : String, mail_address : String, favourite_books : String, sub_id : String, date_added : String)
case class book_title (title_id : Int, name : String, writer : String, year : String, img_url : String, genre : String, copy_amount : Int, owners : String)
case class book_copy (copy_id : Int, serial_number : String, title_id : Int, last_office : Int, first_employee : Int, current_owner : Int, first_date : String, return_dat : String, history : String, date_added : String)
