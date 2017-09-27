import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TreeModule } from 'angular-tree-component';
import { AgmCoreModule } from '@agm/core';
import { SelectModule } from 'ng2-select';

import { SharedModule } from '../../shared/shared.module';

import { BugtrackerComponent } from './bugtracker/bugtracker.component';
import { CalendarComponent } from './calendar/calendar.component';
import { CodeeditorComponent } from './codeeditor/codeeditor.component';
import { ContactdetailsComponent } from './contactdetails/contactdetails.component';
import { ContactsComponent } from './contacts/contacts.component';
import { FaqComponent } from './faq/faq.component';
import { FilemanagerComponent } from './filemanager/filemanager.component';
import { FollowersComponent } from './followers/followers.component';
import { ForumComponent } from './forum/forum.component';
import { ForumdiscussionComponent } from './forum/forumdiscussion/forumdiscussion.component';
import { ForumtopicsComponent } from './forum/forumtopics/forumtopics.component';
import { HelpcenterComponent } from './helpcenter/helpcenter.component';
import { InvoiceComponent } from './invoice/invoice.component';
import { ComposeComponent } from './mailbox/compose/compose.component';
import { FolderComponent } from './mailbox/folder/folder.component';
import { MailboxComponent } from './mailbox/mailbox.component';
import { ViewComponent } from './mailbox/view/view.component';
import { PlansComponent } from './plans/plans.component';
import { ProfileComponent } from './profile/profile.component';
import { ProjectsComponent } from './projects/projects.component';
import { ProjectsdetailsComponent } from './projectsdetails/projectsdetails.component';
import { SearchComponent } from './search/search.component';
import { SettingsComponent } from './settings/settings.component';
import { SocialboardComponent } from './socialboard/socialboard.component';
import { TeamviewerComponent } from './teamviewer/teamviewer.component';
import { TimelineComponent } from './timeline/timeline.component';
import { TodolistComponent } from './todolist/todolist.component';
import { VotelinksComponent } from './votelinks/votelinks.component';

const routes: Routes = [
    { path: 'contacts', component: ContactsComponent },
    { path: 'contactdetails', component: ContactdetailsComponent },
    { path: 'projects', component: ProjectsComponent },
    { path: 'projectsdetails', component: ProjectsdetailsComponent },
    { path: 'teamviewer', component: TeamviewerComponent },
    { path: 'socialboard', component: SocialboardComponent },
    { path: 'votelinks', component: VotelinksComponent },
    { path: 'bugtracker', component: BugtrackerComponent },
    { path: 'faq', component: FaqComponent },
    { path: 'helpcenter', component: HelpcenterComponent },
    { path: 'followers', component: FollowersComponent },
    { path: 'settings', component: SettingsComponent },
    { path: 'plans', component: PlansComponent },
    { path: 'filemanager', component: FilemanagerComponent },

    {
        path: 'forum',
        children: [
            { path: '', component: ForumComponent },
            { path: 'topics/:catid', component: ForumtopicsComponent, outlet: 'primary' },
            { path: 'discussion/:topid', component: ForumdiscussionComponent, outlet: 'primary' }
        ]
    },

    {
        path: 'mailbox',
        component: MailboxComponent,
        children: [
            { path: '', redirectTo: 'folder/inbox' },
            { path: 'folder/:folder', component: FolderComponent },
            { path: 'view/:mid', component: ViewComponent },
            { path: 'compose', component: ComposeComponent }
        ]
    },

    { path: 'timeline', component: TimelineComponent },
    { path: 'calendar', component: CalendarComponent },
    { path: 'invoice', component: InvoiceComponent },
    { path: 'search', component: SearchComponent },
    { path: 'todolist', component: TodolistComponent },
    { path: 'profile', component: ProfileComponent },
    { path: 'codeeditor', component: CodeeditorComponent }
];

@NgModule({
    imports: [
        SharedModule,
        RouterModule.forChild(routes),
        TreeModule,
        AgmCoreModule.forRoot({
            apiKey: 'AIzaSyBNs42Rt_CyxAqdbIBK0a5Ut83QiauESPA'
        }),
        SelectModule
    ],
    declarations: [
        BugtrackerComponent,
        CalendarComponent,
        CodeeditorComponent,
        ContactdetailsComponent,
        ContactsComponent,
        FaqComponent,
        FilemanagerComponent,
        FollowersComponent,
        ForumComponent,
        ForumdiscussionComponent,
        ForumtopicsComponent,
        HelpcenterComponent,
        InvoiceComponent,
        ComposeComponent,
        FolderComponent,
        MailboxComponent,
        ViewComponent,
        PlansComponent,
        ProfileComponent,
        ProjectsComponent,
        ProjectsdetailsComponent,
        SearchComponent,
        SettingsComponent,
        SocialboardComponent,
        TeamviewerComponent,
        TimelineComponent,
        TodolistComponent,
        VotelinksComponent
    ],
    exports: [
        RouterModule
    ]
})
export class ExtrasModule { }

