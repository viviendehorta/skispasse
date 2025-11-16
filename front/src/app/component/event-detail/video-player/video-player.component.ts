import {Component, Input} from '@angular/core';
import {VgCoreModule} from "@videogular/ngx-videogular/core";
import {VgOverlayPlayModule} from "@videogular/ngx-videogular/overlay-play";
import {VgBufferingModule} from "@videogular/ngx-videogular/buffering";
import {VgControlsModule} from "@videogular/ngx-videogular/controls";

@Component({
    selector: "sk-video-player",
    templateUrl: "./video-player.component.html",
    imports: [
        VgCoreModule,
        VgOverlayPlayModule,
        VgBufferingModule,
        VgControlsModule,
    ],
    standalone: true
})
export class VideoPlayerComponent {

    @Input() videoUrl: string;
    @Input() contentType: string;
}
