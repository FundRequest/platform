import {makeTypedFactory, TypedRecord} from 'typed-immutable-record';

export interface UserIdentity {
  provider: string;
  username: string;
}

interface IUser {
  userId: string,
  email: string
  userIdentities: UserIdentity[],
  picture: string
}

export interface IUserRecord extends TypedRecord<IUserRecord>, IUser {
}

export const createUser = makeTypedFactory<IUser, IUserRecord>({
  userId: '',
  email: '',
  userIdentities: null,
  picture: ''
});
