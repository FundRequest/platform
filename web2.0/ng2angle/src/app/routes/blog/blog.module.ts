import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SelectModule } from 'ng2-select';

import { SharedModule } from '../../shared/shared.module';
import { ListComponent } from './list/list.component';
import { PostComponent } from './post/post.component';
import { ArticlesComponent } from './articles/articles.component';
import { ArticleviewComponent } from './articleview/articleview.component';

const routes: Routes = [
    { path: 'list', component: ListComponent },
    { path: 'post', component: PostComponent },
    { path: 'articles', component: ArticlesComponent },
    { path: 'articleview', component: ArticleviewComponent }
];

@NgModule({
    imports: [
        SharedModule,
        RouterModule.forChild(routes),
        SelectModule
    ],
    declarations: [
        ListComponent,
        PostComponent,
        ArticlesComponent,
        ArticleviewComponent
    ],
    exports: [
        RouterModule
    ]
})
export class BlogModule { }