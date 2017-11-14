import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FundModalComponent } from './fund-modal.component';

describe('FundModalComponent', () => {
  let component: FundModalComponent;
  let fixture: ComponentFixture<FundModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FundModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FundModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
