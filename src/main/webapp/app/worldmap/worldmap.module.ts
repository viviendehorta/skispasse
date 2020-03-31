import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SkispasseSharedModule } from 'app/shared/shared.module';
import { MapModule } from 'app/map/map.module';
import { WORLDMAP_ROUTE } from './worldmap.route';
import { WorldmapComponent } from './worldmap.component';

@NgModule({
  declarations: [WorldmapComponent],
  imports: [SkispasseSharedModule, MapModule, RouterModule.forChild([WORLDMAP_ROUTE])]
})
export class SkispasseWorldmapModule {}
