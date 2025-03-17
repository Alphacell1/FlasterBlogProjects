import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import {LoginComponent} from './components/login/login.component'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  standalone: true,
  imports: [RouterModule] 
})
export class AppComponent {
  title = 'blog-flaster-frontend';
}
