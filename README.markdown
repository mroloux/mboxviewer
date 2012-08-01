This is my implementation of CloudSpokes challenge [http://www.cloudspokes.com/challenges/1639](http://www.cloudspokes.com/challenges/1639).

A live version of this application runs at [http://mymboxviewer.appspot.com/](http://mymboxviewer.appspot.com/). Sample mbox files to upload are at [https://github.com/mroloux/mboxviewer/tree/master/test/data](https://github.com/mroloux/mboxviewer/tree/master/test/data).

Features
=

* uploaded mbox files are converted to HTML.
* converted HTML file is a standalone file. No dependencies on external CSS, Javascript or images.
* converted HTML shows an overview of all emails (subject, sender and date)
* e-mail attachments are listed in the modal window that contains the e-mail contents
* during conversion, HTML e-mails are sanitized: Javascript code and certain tags (e.g. &lt;body&gt;) are stripped
* fully unit tested

Libraries used
=

* [Play framework 1.2.5](http://www.playframework.org/)
* [mime4j](http://james.apache.org/mime4j/)
* [OWASP Java Html Sanitizer](https://www.owasp.org/index.php/OWASP_Java_HTML_Sanitizer_Project)

Installation
=

* Clone the git repository at [https://github.com/mroloux/mboxviewer](https://github.com/mroloux/mboxviewer)
* Download and install [Play framework 1.2.5](http://www.playframework.org/download)
* From a console, do "play dependencies --sync"
* From a console, do "play run"
* Browse to localhost:9000