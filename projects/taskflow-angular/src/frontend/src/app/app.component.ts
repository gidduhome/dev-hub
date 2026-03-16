import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './components/sidebar/sidebar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent],
  template: `
    <div class="flex min-h-screen">
      <app-sidebar />
      <main class="ml-56 flex-1 p-8">
        <router-outlet />
      </main>
    </div>
  `
})
export class AppComponent {}
