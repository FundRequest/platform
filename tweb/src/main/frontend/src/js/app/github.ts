export class Github {

    public static validateLink(link: string) {
        let matches = /^https:\/\/github\.com\/(.+)\/(.+)\/issues\/(\d+)$/.exec(link);
        return matches && matches.length >= 4;
    }

    public static getGithubInfo(link: string): Promise<any> {
        let matches = /^https:\/\/github\.com\/(.+)\/(.+)\/issues\/(\d+)$/.exec(link);
        if (matches && matches.length >= 4) {
            let url = `https://api.github.com/repos/${matches[1]}/${matches[2]}/issues/${matches[3]}`;
            return fetch(url).then(res => res.json());
        }
    }


}