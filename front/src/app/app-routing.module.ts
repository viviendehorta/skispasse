import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NewsfactsMapComponent} from "./world-view/newsfacts-map/newsfacts-map.component";

const routes: Routes = [
  {
    path: "",
    component: NewsfactsMapComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
