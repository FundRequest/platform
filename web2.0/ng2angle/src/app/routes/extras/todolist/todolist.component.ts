import { Component, OnInit } from '@angular/core';
declare var $: any;

@Component({
    selector: 'app-todolist',
    templateUrl: './todolist.component.html',
    styleUrls: ['./todolist.component.scss']
})
export class TodolistComponent implements OnInit {

    items = [
        {
            todo: { title: 'Meeting with Mark at 7am.', description: 'Pellentesque convallis mauris eu elit imperdiet quis eleifend quam aliquet. ' },
            complete: true
        },
        {
            todo: { title: 'Call Sonya. Talk about the new project.', description: '' },
            complete: false
        },
        {
            todo: { title: 'Find a new place for vacations', description: '' },
            complete: false
        }
    ];

    editingTodo = false;
    todo: any = {};


    constructor() { }

    ngOnInit() {
    }

    addTodo() {

        if (this.todo.title === '') return;
        if (!this.todo.description) this.todo.description = '';

        if (this.editingTodo) {
            this.todo = {};
            this.editingTodo = false;
        }
        else {
            this.items.push({ todo: $.extend({}, this.todo), complete: false });
            this.todo.title = '';
            this.todo.description = '';
        }
    };

    editTodo(index, $event) {
        $event.preventDefault();
        $event.stopPropagation();
        this.todo = this.items[index].todo;
        this.editingTodo = true;
    };

    removeTodo(index/*, $event*/) {
        this.items.splice(index, 1);
    };

    clearAll() {
        this.items = [];
    };

    totalCompleted() {
        return this.items.filter(item => {
            return item.complete;
        }).length;
    };

    totalPending() {
        return this.items.filter(item => {
            return !item.complete;
        }).length;
    };

}
