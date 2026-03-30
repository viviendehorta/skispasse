import {Component, Input} from '@angular/core';
import {VgBufferingModule} from "@videogular/ngx-videogular/buffering";
import {VgControlsModule} from "@videogular/ngx-videogular/controls";
import {VgCoreModule} from "@videogular/ngx-videogular/core";
import {VgOverlayPlayModule} from "@videogular/ngx-videogular/overlay-play";

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
