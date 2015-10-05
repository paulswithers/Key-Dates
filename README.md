# Key-Dates

Repository showing development from basic XPages to advanced Java MVC XPages to OSGi Domino Web App to CrossWorlds Liberty App
. See http://www.intec.co.uk/tag/xpages-to-web-app-tutorial/

## Setup
The XPages advanced, OSGi and CrossWorlds versions of the application all point to a separate data database. They expect to find the database at XPagesSamples/KeyDates.nsf. This can be changed by editing the source code and packaging the applications again, but for ease, if at all possible, install the initial NSF at that location.

For those who need to change the configuration, it is held in the xsp.properties of the XPages advanced application and in WebContent\WEB-INF\web.xml for the OSGi and CrossWorlds applications.

## Source Code Architecture
- **xpages-basic** contains the On Disk Project for the NSF that should be used as the data database for all others
- **xpages-java** contains the On Disk Project for the more advanced XPages application. This is just an XPages interface. No data is stored in this NSF. Instead it is stored in an NSF defined in the xsp.properties with the variable **dataDbPath**, by default set as XPagesSamples/KeyDates.nsf. It uses Java as the backend. The Javadoc for the application can be found in **documentation/xpages-java**.
- **osgiworlds** contains the Plugin project for the Vaadin OSGi application. It requires OpenNTF Domino API 2.0.0 or higher and incorporates the code from the OsgiWorlds project on [OpenNTF] (http://www.openntf.org/main.nsf/project.xsp?r=project/OsgiWorlds). This is a standard Eclipse, non-Mavenized plugin project. The Javadoc for the application can be found in **documentation/osgiworlds**.
- **osgiworlds-feature** is the Feature project for the osgiworlds version of the application.
- **osgiworlds-update** is the Update Site project for the osgiworlds version of the application.
- **crossworlds** contains the Mavenized Vaadin web application for running on CrossWorlds. It requires a CrossWorlds instance running OpenNTF Domino API 2.0.0 or higher. The Javadoc for the application can be found in **documentation/crossworlds**
- **documentation** contains the Javadocs for the Java-based applications and the documentation for OpenNTF.

