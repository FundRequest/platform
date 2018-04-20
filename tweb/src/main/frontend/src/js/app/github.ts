import {Utils} from './Utils';

import {Contracts} from "./contracts";
import {FundRepository} from "../contracts/FundRepository";

export class GithubUser {
    login: string;
    id: number;
    avatar_url: string;
    gravatar_id: string;
    url: string;
    html_url: string;
    followers_url: string;
    following_url: string;
    gists_url: string;
    starred_url: string;
    subscriptions_url: string;
    organizations_url: string;
    repos_url: string;
    events_url: string;
    received_events_url: string;
    type: string;
    site_admin: boolean;
}

export class GithubIssue {
    url: string;
    repository_url: string;
    labels_url: string;
    comments_url: string;
    events_url: string;
    html_url: string;
    id: number;
    number: number;
    title: string;
    user: GithubUser;
    labels: string[];
    state: string;
    locked: boolean;
    assignee: boolean;
    assignees: string[];
    milestone: string;
    comments: number;
    created_at: string; //2018-03-21T10:28:54Z,
    updated_at: string; // 2018-03-22T17:04:24Z,
    closed_at: string;
    author_association: string;
    body: string;
    closed_by: string;

    // extra params
    readonly platform: string = 'GITHUB';
    owner: string;
    repo: string;
    platformId: string;
    avatar: string;

    constructor(res: any) {
        Object.assign(this, res);
        let matches = /^https:\/\/github\.com\/(.+)\/(.+)\/issues\/(\d+)$/.exec(this.html_url);
        this.owner = matches[1];
        this.repo = matches[2];
        this.platformId = `${this.owner}|FR|${this.repo}|FR|${this.number}`;
        this.avatar = `https://github.com/${this.owner}.png`;
    }
}

export class Github {

    private static _tokenContractAddress = Contracts.getInstance().tokenContractAddress;
    private _fundRepository: FundRepository;

    constructor() {
        this._init();
    }

    private async _init() {
        await Promise.all([
            Contracts.getInstance().getFundRepository().then(c => this._fundRepository = c)
        ]);
    }

    public static async validateLink(link: string): Promise<boolean> {
        return (await Github.getGithubInfo(link)) != null;
    }

    public static getGithubInfo(link: string): Promise<GithubIssue> {
        let matches = /^https:\/\/github\.com\/(.+)\/(.+)\/issues\/(\d+)$/.exec(link);
        if (matches && matches.length >= 4) {
            let url = `https://api.github.com/repos/${matches[1]}/${matches[2]}/issues/${matches[3]}`;
            return Utils.fetchJSON(url)
                .then(res => res ? new GithubIssue(res) : null);
        } else {
            return Promise.resolve(null);
        }
    }

    public static getPlatformId(githubIssue: GithubIssue) {
        return githubIssue.platformId;
    }
}
