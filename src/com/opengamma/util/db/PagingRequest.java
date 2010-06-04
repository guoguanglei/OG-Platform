/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.util.db;

import com.opengamma.util.ArgumentChecker;

/**
 * Simple immutable model for managing paging, as typically used on websites.
 */
public final class PagingRequest {

  /**
   * Singleton constant to request all items (no paging).
   */
  public static final PagingRequest ALL = new PagingRequest(1, Integer.MAX_VALUE);
  /**
   * A default size for paging.
   */
  private static final int DEFAULT_PAGING_SIZE = 20;

  /**
   * The requested page.
   */
  private final int _page;
  /**
   * The requested page size.
   */
  private final int _pagingSize;

  /**
   * Obtains an instance, applying default values.
   * @param page  the page number, page one chosen if zero, not negative
   * @param pagingSize  the paging size, size twenty chosen if zero, not negative
   * @return the paging request, not null
   * @throws IllegalArgumentException if either input is negative
   */
  public static PagingRequest of(int page, int pagingSize) {
    page = (page == 0 ? 1 : page);
    pagingSize = (pagingSize == 0 ? DEFAULT_PAGING_SIZE : pagingSize);
    return new PagingRequest(page, pagingSize);
  }

  /**
   * Creates an instance for page one and size 20.
   */
  public PagingRequest() {
    this(1, DEFAULT_PAGING_SIZE);
  }

  /**
   * Creates an instance for the specified page using size 20.
   * @param page  the page number, one or greater
   * @throws IllegalArgumentException if input is negative or zero
   */
  public PagingRequest(final int page) {
    this(page, 20);
  }

  /**
   * Creates an instance.
   * @param page  the page number, one or greater
   * @param pagingSize  the paging size, one or greater
   * @throws IllegalArgumentException if either input is negative or zero
   */
  public PagingRequest(final int page, final int pagingSize) {
    ArgumentChecker.notNegativeOrZero(page, "page number");
    ArgumentChecker.notNegativeOrZero(pagingSize, "paging size");
    _page = page;
    _pagingSize = pagingSize;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the one-based page number.
   * @return the page number, one or greater
   */
  public int getPage() {
    return _page;
  }

  /**
   * Gets the size of each page.
   * @return the paging size, one or greater
   */
  public int getPagingSize() {
    return _pagingSize;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the first item, using a one-based index.
   * @return the first item number, one-based
   */
  public int getFirstItem() {
    return (_page - 1) * _pagingSize + 1;
  }

  /**
   * Gets the first item, using a zero-based index.
   * @return the first item index, zero-based
   */
  public int getFirstItemIndex() {
    return getFirstItem() - 1;
  }

  /**
   * Gets the last item inclusive, using a one-based index.
   * @return the last item number, inclusive, one-based
   */
  public int getLastItem() {
    return _page * _pagingSize;
  }

  /**
   * Gets the last item exclusive, using a zero-based index.
   * @return the last item index, exclusive, zero-based
   */
  public int getLastItemIndex() {
    return getLastItem();
  }

  //-------------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PagingRequest) {
      PagingRequest other = (PagingRequest) obj;
      return _page == other._page && _pagingSize == other._pagingSize;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return _page << 16 + _pagingSize;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[page=" + _page + ", pagingSize=" + _pagingSize + "]";
  }

}
