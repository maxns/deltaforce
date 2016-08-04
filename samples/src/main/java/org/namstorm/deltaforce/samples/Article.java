package org.namstorm.deltaforce.samples;


import org.namstorm.deltaforce.annotations.DeltaBuilder;

/**
 * This JavaBean represents an Article in the On-line Store application.
 *
 * @author deors
 * @version 1.0
 */
@DeltaBuilder
public class Article {

    /** Active status string. */
    private static final String ACTIVE = "active";

    /** Inactive status string. */
    private static final String INACTIVE = "inactive";

    /** Invalid status string. */
    private static final String INVALID = "invalid";

    /** The unique identifier for this Article in the catalog. */
    private String id;

    /** The department number where this Article belongs to. */
    private int department;

    /** Textual representation of this Article status. */
    private String status;

    /**
     * Default constructor.
     */
    public Article() {
        super();
    }


}
