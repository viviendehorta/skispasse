import {Component, Input} from '@angular/core';

@Component({
    selector: 'skis-video-player',
    templateUrl: './video-player.component.html'
})
export class VideoPlayerComponent {

    @Input() videoUrl: string;
    @Input() contentType: string;
}
