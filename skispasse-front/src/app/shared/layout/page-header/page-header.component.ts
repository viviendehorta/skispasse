import {Component, Input, OnInit} from '@angular/core';
import {Location} from "@angular/common";

@Component({
  selector: 'skis-page-header',
  templateUrl: './page-header.component.html',
  styleUrls: ['./page-header.component.scss']
})
export class PageHeaderComponent implements OnInit {

  @Input() headerLabel: string;

  constructor(private location: Location) { }

  ngOnInit() {
  }

  goBack() {
    this.location.back();
  }
}
