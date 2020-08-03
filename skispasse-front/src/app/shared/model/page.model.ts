export interface IPage {
  items: any[];
  itemCount: any;
  links: any;
}

export class Page implements IPage {
  items: any[];
  itemCount: any;
  links: any;

  constructor(items: any[], itemCount: any, links: any) {
    this.items = items;
    this.itemCount = itemCount;
    this.links = links;
  }
}
