import { Injectable } from '@angular/core';
import { SessionStorageService } from 'ngx-webstorage';

@Injectable({ providedIn: 'root' })
export class StateStorageService {

  private previousUrlKey = 'previousUrl';

  constructor(private $sessionStorage: SessionStorageService) {}

  storeUrl(url: string) {
    this.$sessionStorage.store(this.previousUrlKey, url);
  }

  getUrl() {
    return this.$sessionStorage.retrieve(this.previousUrlKey);
  }

  clearUrl(): void {
    this.$sessionStorage.clear(this.previousUrlKey);
  }
}
