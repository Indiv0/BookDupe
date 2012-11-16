Description
  BookDupe is a plugin for bukkit which is used for copying written books without significant effort.

Permission
  NONE
Commands
  NONE

Usage
  Simply use one of the two new recipes in order to create a duplicate of a book:
    Signed (written) book + Book & Quill = 2x Signed (written) book
    Signed (written) book + book + feather + ink sack = 2x Signed (written) book    

Source
  https://github.com/Indiv0/BookDupe
Download
  https://github.com/downloads/Indiv0/BookDupe/BookDupe.jar

Changelog
  1.2
    Added secondary recipe to craft multiple books
    Added shift-click support to secondary recipe
  1.1
    Fixed a bug resulting from clicking the result slot twice in a row
  1.0
    Books successfully duplicate when placed as a shapeless recipe

Verified compatibility
  1.2
    CB 1.3.2 RB-1.0

Bugs
  None known.

ToDo
  Reimplement core functionality through NBT changes rather than the current ItemStack.clone()