import {Injectable} from '@angular/core';
import {PaginationService} from './pagination.service';

@Injectable({providedIn: 'root'})
export class ResolvePaginationParamService {

  constructor(private paginationUtil: PaginationService) {
  }

  resolve(route, state) {
    const page = route.queryParams.page ? route.queryParams.page : '1';
    const defaultSort = route.data.defaultSort ? route.data.defaultSort : 'id,asc';
    const sort = route.queryParams.sort ? route.queryParams.sort : defaultSort;
    return {
      page: this.paginationUtil.parsePage(page),
      predicate: this.paginationUtil.parsePredicate(sort),
      ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}
