import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TreeModule } from 'angular-tree-component';
import { DndModule } from 'ng2-dnd';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

import { SharedModule } from '../../shared/shared.module';
import { ButtonsComponent } from './buttons/buttons.component';
import { SpinnersComponent } from './spinners/spinners.component';
import { DropdownComponent } from './dropdown/dropdown.component';
import { GridComponent } from './grid/grid.component';
import { GridmasonryComponent } from './gridmasonry/gridmasonry.component';
import { TypographyComponent } from './typography/typography.component';
import { IconsfontComponent } from './iconsfont/iconsfont.component';
import { IconsweatherComponent } from './iconsweather/iconsweather.component';
import { ColorsComponent } from './colors/colors.component';
import { InteractionComponent } from './interaction/interaction.component';
import { NotificationComponent } from './notification/notification.component';
import { NavtreeComponent } from './navtree/navtree.component';
import { SortableComponent } from './sortable/sortable.component';
import { InfinitescrollComponent } from './infinitescroll/infinitescroll.component';
import { SweetalertComponent } from './sweetalert/sweetalert.component';

const routes: Routes = [
    { path: 'buttons', component: ButtonsComponent },
    { path: 'interaction', component: InteractionComponent },
    { path: 'notification', component: NotificationComponent },
    { path: 'sweetalert', component: SweetalertComponent },
    { path: 'spinners', component: SpinnersComponent },
    { path: 'dropdown', component: DropdownComponent },
    { path: 'navtree', component: NavtreeComponent },
    { path: 'sortable', component: SortableComponent },
    { path: 'grid', component: GridComponent },
    { path: 'gridmasonry', component: GridmasonryComponent },
    { path: 'typography', component: TypographyComponent },
    { path: 'iconsfont', component: IconsfontComponent },
    { path: 'iconsweather', component: IconsweatherComponent },
    { path: 'colors', component: ColorsComponent },
    { path: 'infinitescroll', component: InfinitescrollComponent }
];

@NgModule({
    imports: [
        SharedModule,
        RouterModule.forChild(routes),
        TreeModule,
        DndModule.forRoot(),
        InfiniteScrollModule
    ],
    declarations: [
        ButtonsComponent,
        SpinnersComponent,
        DropdownComponent,
        GridComponent,
        GridmasonryComponent,
        TypographyComponent,
        IconsfontComponent,
        IconsweatherComponent,
        ColorsComponent,
        InteractionComponent,
        NotificationComponent,
        NavtreeComponent,
        SortableComponent,
        InfinitescrollComponent,
        SweetalertComponent
    ],
    exports: [
        RouterModule
    ]
})
export class ElementsModule { }
