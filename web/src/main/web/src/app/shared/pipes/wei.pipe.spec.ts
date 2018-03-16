import { WeiAsNumberPipe, WeiPipe } from './wei.pipe';

describe('WeiPipe', () => {
  it('create an instance', () => {
    const pipe = new WeiPipe();
    expect(pipe).toBeTruthy();
  });
});

describe('WeiAsNumberPipe', () => {
  it('create an instance', () => {
    const pipe = new WeiAsNumberPipe();
    expect(pipe).toBeTruthy();
  });
});
