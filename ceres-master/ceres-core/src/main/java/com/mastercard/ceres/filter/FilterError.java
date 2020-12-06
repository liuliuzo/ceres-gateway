package com.mastercard.ceres.filter;

/**
 * @className FilterError
 * @description
 * @author liuliu
 * @version 1.0
 * @email liuliu.zhao@mastercard.com
 */
public class FilterError implements Cloneable {

    private String filterName;
    private String filterType;
    private Throwable exception = null;

    public FilterError(String filterName, String filterType, Throwable exception) {
        this.filterName = filterName;
        this.filterType = filterType;
        this.exception = exception;
    }

    public String getFilterName() {
        return filterName;
    }

    public String getFilterType() {
        return filterType;
    }

    public Throwable getException() {
        return exception;
    }

    @Override
    public Object clone() {
        return new FilterError(filterName, filterType, exception);
    }

    @Override
    public String toString() {
        return "FilterError [filterName=" + filterName + ", filterType=" + filterType + ", exception=" + exception+ "]";
    }
}