import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveModalComponent } from './approve-modal.component';

describe('ApproveModalComponent', () => {
  let component: ApproveModalComponent;
  let fixture: ComponentFixture<ApproveModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApproveModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
