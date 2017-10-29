import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FundButtonComponent } from './fund-button.component';

describe('FundButtonComponent', () => {
  let component: FundButtonComponent;
  let fixture: ComponentFixture<FundButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FundButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FundButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
