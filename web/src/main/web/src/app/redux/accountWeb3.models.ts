import {makeTypedFactory, TypedRecord} from 'typed-immutable-record';

interface IAccountWeb3 {
  networkId: string,
  network: string,
  balance: '0',
  currentAccount: '0x0000000000000000000000000000000000000000',
  locked: boolean,
  supported: boolean,
  disabled: boolean,
  web3: any
}

export interface IAccountWeb3Record extends TypedRecord<IAccountWeb3Record>, IAccountWeb3 {
}

export const createAccountWeb3 = makeTypedFactory<IAccountWeb3, IAccountWeb3Record>({
  networkId: '',
  network: '',
  balance: '0',
  currentAccount: '0x0000000000000000000000000000000000000000',
  locked: true,
  supported: false,
  disabled: true,
  web3: null
});
