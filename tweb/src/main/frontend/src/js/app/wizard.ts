import {Utils} from './utils';
import {Github, GithubIssue} from './github';

class ControlButton {
    private readonly _el: HTMLElement;
    private readonly _defaultText: any;

    constructor(buttonEl, clickEvent) {
        this._el = buttonEl;
        if (this._el != null) {
            this._defaultText = this._el.innerHTML;
            this._el.addEventListener('click', (e) => clickEvent(e));
        }
    }

    public hide() {
        if (this._el != null) {
            this._el.classList.remove('disabled');
            this._el.classList.add('d-none');
        }
    }

    public show() {
        if (this._el != null) {
            this._el.classList.remove('d-none', 'disabled');
        }
    }

    public disable() {
        if (this._el != null) {
            this._el.classList.remove('d-none');
            this._el.classList.add('disabled');
        }
    }

    public enable() {
        if (this._el != null) {
            this._el.classList.remove('d-none', 'disabled');
        }
    }

    public updateText(text: string) {
        if (this._el != null) {
            if (text) {
                this._el.innerHTML = text;
            } else {
                this._el.innerHTML = this._defaultText;
            }
        }
    }
}

interface Step {
    stepIcon: StepIcon;
    panel: Panel;

    setNextStep(step: Step);

    setPreviousStep(step: Step);

    setActive();

    finish();

    goToNext(): Promise<Step>;

    goToPrevious(): Promise<Step>;
}

class StepImpl implements Step {
    public stepIcon: StepIcon;
    public panel: Panel;
    private _title: string;
    private _nextButton: ControlButton;
    private _nextButtonText: string;
    private _previousButton: ControlButton;
    private _previousButtonText: string;
    private _finishedCallback: any;

    private _nextStep: Step = null;
    private _previousStep: Step = null;

    constructor(stepIcon, panel, nextButton, previousButton, finishedCallback = null) {
        this.stepIcon = stepIcon;
        this.panel = panel;
        this._title = stepIcon.getTitle();
        this._nextButton = nextButton;
        this._nextButtonText = this.panel.getEl().dataset.wizardPanelNext;
        this._previousButton = previousButton;
        this._previousButtonText = this.panel.getEl().dataset.wizardPanelPrevious;
        this._finishedCallback = finishedCallback;
    }

    public setNextStep(step: Step) {
        this._nextStep = step;
    }

    public setPreviousStep(step: Step) {
        this._previousStep = step;
    }

    public setActive() {
        if (this._previousStep != null) {
            this._previousStep.stepIcon.setCompleted();
            this._previousStep.panel.setBackwards();
        }
        this.stepIcon.setActive();
        this.panel.setActive();
        if (this._nextStep != null) {
            this._nextStep.stepIcon.unsetCompleted();
            this._nextStep.panel.setForwards();
        }
        this._updateButtons();
    }

    public finish() {
        this._finishedCallback();
    }

    public async goToNext(): Promise<Step> {
        if (await this.validate()) {
            if (this._nextStep != null) {
                this._nextStep.setActive();
                return this._nextStep;
            } else {
                this.finish();
                return this;
            }
        } else {
            return this;
        }
    }

    public async goToPrevious(): Promise<Step> {
        if (this._previousStep != null) {
            this._previousStep.setActive();
            return this._previousStep;
        } else {
            return this;
        }
    }

    protected async validate(): Promise<boolean> {
        let isValid: boolean = true;
        let panelEl = this.panel.getEl();
        let formElements: HTMLElement[] = Array.from(panelEl.querySelectorAll('[data-form-validation]'));
        for (let fieldElement of formElements) {
            let validations: string[] = fieldElement.dataset.formValidation.split(';');
            isValid = await Utils.validateHTMLElement(fieldElement, validations) && isValid;
        }

        this.panel.updateHeight();
        return isValid;
    }

    private _updateButtons() {
        this._previousButton.updateText(this._previousButtonText);
        this._nextButton.updateText(this._nextButtonText);
        this._previousStep == null ? this._previousButton.hide() : this._previousButton.show();
    }
}

class StepImplGithub extends StepImpl implements Step {
    protected async validate(): Promise<boolean> {
        let isValid: boolean = true;
        let panelEl = this.panel.getEl();
        let formElements: HTMLElement[] = Array.from(panelEl.querySelectorAll('[data-form-validation]'));
        for (let fieldElement of formElements) {
            let validations: string[] = fieldElement.dataset.formValidation.split(';');
            if (validations.includes('github')) {
                isValid = await Utils.validateHTMLElement(fieldElement, validations, async (githubLink: string) => {
                    let info: GithubIssue = await Github.getGithubInfo(githubLink);
                    let title = panelEl.parentElement.parentElement.querySelector('[data-wizard-title]');
                    if (title) {
                        if (info == null) {
                            title.innerHTML = '';
                        } else {
                            title.innerHTML = `<img src="${info.avatar}?size=30" height="30" width="30" /> 
                                               <a href="${info.html_url}" target="_blank" rel="noopener noreferrer">
                                                   ${info.title} <span class="text-muted">#${info.number}</span>
                                               </a>`;
                        }
                    }
                }) && isValid;
            } else {
                isValid = await Utils.validateHTMLElement(fieldElement, validations) && isValid;
            }
        }
        this.panel.updateHeight();
        return isValid;
    }
}

class StepIcon {
    private readonly _el: HTMLElement;
    private readonly _title: string;

    constructor(stepEl) {
        this._el = stepEl;
        this._title = this._el.dataset.wizardStep;
    }

    public getEl() {
        return this._el;
    }

    public getTitle() {
        return this._title;
    }

    public setCompleted() {
        this._el.classList.remove('-active');
        this._el.classList.add('-completed');
    }

    public unsetCompleted() {
        this._el.classList.remove('-active', '-completed');
    }

    public setActive() {
        this._el.classList.remove('-completed');
        this._el.classList.add('-active');
    }
}

class Panel {
    private readonly _el: HTMLElement;
    private readonly _wizardEl: HTMLFormElement;

    constructor(wizardEl, panelEl) {
        this._wizardEl = wizardEl;
        this._el = panelEl;
    }

    public getEl() {
        return this._el;
    }

    public setActive() {
        this._el.classList.remove('movingOutBackward', 'movingOutForward');
        this._el.classList.add('movingIn');
        this.updateHeight();
    }

    public setBackwards() {
        this._el.classList.remove('movingIn', 'movingOutForward');
        this._el.classList.add('movingOutBackward');
    }

    public setForwards() {
        this._el.classList.remove('movingIn', 'movingOutBackward');
        this._el.classList.add('movingOutForward');
    }

    public updateHeight() {
        this._el.parentElement.style.height = `${this._el.offsetHeight}px`;
    }
}

class Steps {
    private _el: HTMLFormElement;
    private _buttonNext: ControlButton;
    private _buttonPrevious: ControlButton;
    private readonly _stepsEl: HTMLElement[];
    private readonly _panelsEl: HTMLElement[];
    private _steps: Step[];

    public currentStep: Step;
    public numberOfSteps: number;

    constructor(wizardEl: HTMLFormElement, buttonNext: HTMLElement, buttonPrevious: HTMLElement, finishedCallback) {
        this._el = wizardEl;
        this._stepsEl = Array.from(this._el.querySelectorAll('[data-wizard-step'));
        this._panelsEl = Array.from(this._el.querySelectorAll('[data-wizard-panel]'));
        this.numberOfSteps = this._stepsEl.length;
        this._initButtons(buttonNext, buttonPrevious);
        this._initSteps(finishedCallback);
    }

    public async goToNextStep() {
        this.currentStep = await this.currentStep.goToNext();
    }

    public async goToPreviousStep() {
        this.currentStep = await this.currentStep.goToPrevious();
    }

    private _setCurrentStep(step: Step) {
        this.currentStep = step;
        this.currentStep.setActive();
    }

    private _initSteps(finishedCallback) {
        this._steps = [];
        let previousStep = null;

        for (let i = 0; i < this.numberOfSteps; i++) {
            let stepIcon: StepIcon = new StepIcon(this._stepsEl[i]);
            let panel: Panel = new Panel(this._el, this._panelsEl[i]);
            let step: Step;

            switch ((<any>panel.getEl().dataset).wizardPanel) {
                case 'github':
                    step = new StepImplGithub(stepIcon, panel, this._buttonNext, this._buttonPrevious);
                    break;
                default:
                    step = new StepImpl(stepIcon, panel, this._buttonNext, this._buttonPrevious, i + 1 == this.numberOfSteps ? finishedCallback : null);
                    break;
            }

            if (previousStep != null) {
                previousStep.setNextStep(step);
            }

            step.setPreviousStep(previousStep);
            previousStep = step;
            this._steps.push(step);
        }

        this._setCurrentStep(this._steps[0]);
    }

    private _initButtons(buttonNext, buttonPrevious) {
        this._buttonNext = new ControlButton(buttonNext, (e) => {
            e.preventDefault();
            this.goToNextStep();
        });
        this._buttonPrevious = new ControlButton(buttonPrevious, (e) => {
            e.preventDefault();
            this.goToPreviousStep();
        });
    }
}

export class Wizard {
    private readonly _el: HTMLFormElement;
    private readonly _buttonPrevious: HTMLElement;
    private readonly _buttonNext: HTMLElement;

    private _steps: Steps;

    constructor(wizardEl, finishedCallback) {
        this._el = wizardEl;
        this._buttonNext = this._el.querySelector('[data-wizard-control="next"]');
        this._buttonPrevious = this._el.querySelector('[data-wizard-control="previous"]');

        this._steps = new Steps(this._el, this._buttonNext, this._buttonPrevious, finishedCallback);
    }
}