package src.main.java.car.tp2;

import org.apache.commons.net.ftp.FTPFile;

/**
 * HTMLGenerator is a class used to generate HTML tags from data.
 *
 * @author Armand (Tydax) BOUR
 * TODO: implement
 */
public class HTMLGenerator {

    final static private String form_upload = "<div><h1 style='font-size:1.2em; font-family: sans'>Telecharger un fichier</h1><form method='POST' action='http://localhost:8080/rest/tp2/commandes/upload' enctype='multipart/form-data'>\n" +
            "Choisir le fichier<input type='file' name='file'><br> nom de la destination : <input type='hidden' name='path' value='#'><input type='text' name='name' /><br />\n" +
            "<input type='submit' value='Telecharger'>\n" +
            "</form> </div>";

    final static private String header = "<!doctype html>\n<html lang='fr'>\n<head>\n\t<meta charset='utf-8'>\n\t<title>Serveur FTP</title>\n</head>\n<body>";
    final static private String footer = "</body>\n</html>";

    final static private String listTag = "<ul>\n%s\n</ul>";

    /**
     * TODO: implement
     * @param htmlobjs
     * @return
     */
    public static String generateList(final String ... htmlobjs) {
        final StringBuilder listContent = new StringBuilder();

        for (final String obj : htmlobjs) {
            listContent.append("\t<li>")
                       .append(obj)
                       .append("</li>\n");
        }

        return String.format(listTag, listContent.toString());
    }

    /**
     * @param file
     * @return
     */
    public static String generateLinkFromFile(final String cmdUrl, final String folderPath, final FTPFile file) {
        final StringBuilder html = new StringBuilder();
        final StringBuilder downloadLink = new StringBuilder();
        final StringBuilder deleteLink = new StringBuilder();

        // Generate download link
        downloadLink.append(cmdUrl);
        downloadLink.append(file.isDirectory()
                           ? "/list/"
                           : "/get/");
        downloadLink.append(folderPath)
                    .append(file.getName());

        // Generate delete link if necessary
        if (!file.isDirectory()) {
            deleteLink.append("<a href='")
                      .append(cmdUrl)
                      .append("/delete/")
                      .append(folderPath)
                      .append(file.getName())
                      .append("'>Delete</a>");
        }

        // Join all the links together
        html.append("<a href='")
            .append(downloadLink.toString())
            .append("'>")
            .append(file.getName())
            .append(deleteLink.toString())
            .append("</li>");

        return html.toString();
    }

    public static String generateUploadForm() {
        return form_upload;
    }

    public static String generatePage(final String content) {
        final StringBuilder html = new StringBuilder(header);
        html.append(header)
            .append(content)
            .append(footer);

        return html.toString();
    }

}
