package model;

import io.advantageous.boon.json.annotations.JsonInclude;

/**
 * @author Ilya Ismagilov <ilya@singulator.net>
 */
public class Todo {
    @JsonInclude
    public long id;
    @JsonInclude
    public String title;
    @JsonInclude
    public boolean completed;
    @JsonInclude
    public int order;
    @JsonInclude
    public String url;
}
