# jatch

Scratch Studio
https://scratch.mit.edu/studios/4649692/

.SB2 Files Download
https://www.dropbox.com/s/puangcb94hr9lz6/exampleProjects.zip?dl=0

Command Translation Spreadsheet:
https://docs.google.com/spreadsheets/d/1-Yy92X6SYYLudNhNaUyr7_qPjbD7TpUQwPnhX8cjMV8

To run:

  1. Download and add project to eclipse.
  2. In your Scratch project, click "File>Download as .sb2". (Note: does not work on Safari.)
  3. Import your .sb2 file into the eclipse project, at the outermost level (the same directory that has src/ in it.)
  4. Open main.java.jatch.Main. Run it. You should get an error.
  5. Go to Run>Run Configurations. Click arguments. Add the name of your .sb2 in program arguments.
  6. Run Main again. It should spit out some text into the console.
  7. Right-click the src/ folder and click "refresh".
  8. Your newly compiled project is in compiled/. Run the Game class.
