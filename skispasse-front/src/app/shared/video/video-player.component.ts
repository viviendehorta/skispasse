import {Component, Input} from '@angular/core';

@Component({
    selector: 'skis-video-player',
    templateUrl: './video-player.component.html',
    styleUrls: [
        './video-player.component.scss'
    ]
})
export class VideoPlayerComponent {

    @Input() videoUrl: string;
}
