import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FindAidsComponent} from "./find-aids/find-aids.component";

const routes: Routes = [
  {
    path: "",
    component: FindAidsComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
