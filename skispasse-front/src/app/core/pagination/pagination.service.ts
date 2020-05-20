import {Injectable} from '@angular/core';

@Injectable({ providedIn: 'root' })
export class PaginationService {

  parseHeaderLinks(header): any {
    if (header.length === 0) {
      throw new Error('input must not be of zero length');
    }
    // Split parts by comma
    const parts = header.split(',');
    const links = {};
    // Parse each part into a named link
    parts.forEach(p => {
      const section = p.split(';');
      if (section.length !== 2) {
        throw new Error('section could not be split on ";"');
      }
      const url = section[0].replace(/<(.*)>/, '$1').trim();
      const queryString = {};
      url.replace(new RegExp('([^?=&]+)(=([^&]*))?', 'g'), ($0, $1, $2, $3) => (queryString[$1] = $3));
      // @ts-ignore
      let page = queryString.page;
      if (typeof page === 'string') {
        page = parseInt(page, 10);
      }
      const name = section[1].replace(/rel="(.*)"/, '$1').trim();
      links[name] = page;
    });
    return links;
  }

  /**
   * Method to find whether the sort is defined
   */
  parseAscending(sort) {
    let sortArray = sort.split(',');
    sortArray = sortArray.length > 1 ? sortArray : sort.split('%2C');
    if (sortArray.length > 1) {
      return sortArray.slice(-1)[0] === 'asc';
    }
    // default to true if no sort is defined
    return true;
  }
  /**
   * Method to query params are strings, and need to be parsed
   */
  parsePage(page) {
    return parseInt(page, 10);
  }
  /**
   * Method to sort can be in the format `id,asc` or `id`
   */
  parsePredicate(sort) {
    return sort.split(',')[0].split('%2C')[0];
  }
}
