import {AfterContentInit, ContentChild, Directive, HostListener, Input} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {IconDefinition} from '@fortawesome/free-solid-svg-icons';
import {SortDirective} from './sort.directive';
import {AppConfigService} from '../../core/config/app-config.service';


@Directive({
  selector: '[skisSortBy]'
})
export class SortByDirective implements AfterContentInit {

  private skisSort;

  @Input() skisSortBy: string;

  @ContentChild(FaIconComponent, { static: true }) iconComponent: FaIconComponent;

  sortIcon: IconDefinition;
  sortAscIcon: IconDefinition;
  sortDescIcon: IconDefinition;

  constructor(skisSort: SortDirective, configService: AppConfigService) {
    this.skisSort = skisSort;
    this.skisSort = skisSort;
    const config = configService.getConfig();
    this.sortIcon = config.sortIcon;
    this.sortAscIcon = config.sortAscIcon;
    this.sortDescIcon = config.sortDescIcon;
  }

  ngAfterContentInit(): void {
    if (this.skisSort.predicate && this.skisSort.predicate !== '_score' && this.skisSort.predicate === this.skisSortBy) {
      this.updateIconDefinition(this.iconComponent, this.skisSort.ascending ? this.sortAscIcon : this.sortDescIcon);
      this.skisSort.activeIconComponent = this.iconComponent;
    }
  }

  @HostListener('click', [])
  onClick(): void {
    if (this.skisSort.predicate && this.skisSort.predicate !== '_score') {
      this.skisSort.sort(this.skisSortBy);
      this.updateIconDefinition(this.skisSort.activeIconComponent, this.sortIcon);
      this.updateIconDefinition(this.iconComponent, this.skisSort.ascending ? this.sortAscIcon : this.sortDescIcon);
      this.skisSort.activeIconComponent = this.iconComponent;
    }
  }

  private updateIconDefinition(iconComponent, icon) {
    if (iconComponent) {
      iconComponent.iconProp = icon;
      iconComponent.ngOnChanges({});
    }
  }
}
