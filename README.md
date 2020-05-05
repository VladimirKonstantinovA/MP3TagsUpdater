# MP3TagsUpdater
MP3 tags updater

Updating mp3 tags(Song name, Album, Artist, Cover) for directories files.

Hierarchy:

  <(Some dir)>

     <(Album name)> - Album level dir
   
        <(Artist name)> - Artist level dir

           <(Song name).(song extension)> - audio file

Give command line parameters(in order):
  - directory for search and update files
  - extensions filter for audio files(sample: "mp3"(without symbol (") ))
  - extensions filter for image files(sample: "jpg"(without symbol (") ))
