package org.altervista.alecat.swimmanager.interfaces;

import org.altervista.alecat.swimmanager.error.PdfStructureException;
import org.altervista.alecat.swimmanager.models.Rank;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alessandro Cattapan on 26/12/2017.
 */

public interface FinPdfReader {

    /**
     * Returns the total number of pages of the PDF
     * @return numPages
     */
    public int getNumberPages();

    /**
     * Returns the page's content
     * @param number
     * @return pageContent
     */
    public String getPageContent(int number) throws IOException;

    /**
     * Returns a rank of a competition
     * @param page
     * @return rank
     */
    public Rank getRank(String page) throws IOException;

    /**
     * This method search all ranks inside the pdf and it merges ranks of the same race
     * @return listOfRankMerged
     */
    public ArrayList<Rank> getAllRank() throws IOException;
}
