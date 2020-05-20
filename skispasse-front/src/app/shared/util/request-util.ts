import { HttpParams } from '@angular/common/http';

export const createHttpPagingOptions = (pagingParams?: any): HttpParams => {
  let options: HttpParams = new HttpParams();
  if (pagingParams) {
    Object.keys(pagingParams).forEach(key => {
      if (key !== 'sort') {
        options = options.set(key, pagingParams[key]);
      }
    });
    if (pagingParams.sort) {
      pagingParams.sort.forEach(val => {
        options = options.append('sort', val);
      });
    }
  }
  return options;
};
