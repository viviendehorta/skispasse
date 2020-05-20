import {Directive, EventEmitter, Input, Output} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';


@Directive({
  selector: '[skisSort]'
})
export class SortDirective {

  @Input() predicate: string;
  @Input()  ascending: boolean;
  @Input()  callback: () => void;
  @Output() predicateChange: EventEmitter<string>;
  @Output() ascendingChange: EventEmitter<boolean>;
  @Output() activeIconComponent: FaIconComponent;

  constructor() {
    this.predicateChange = new EventEmitter();
    this.ascendingChange = new EventEmitter();
  }

  sort(field: string): void {
    this.ascending = field !== this.predicate ? true : !this.ascending;
    this.predicate = field;
    this.predicateChange.emit(field);
    this.ascendingChange.emit(this.ascending);
    this.callback();
  }
}
