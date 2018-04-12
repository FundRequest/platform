import {Utils} from './utils';
import {Github, GithubIssue} from './github';

class ControlButton {
    private readonly _el: HTMLElement;
    private readonly _buttonTexts: any;

    constructor(buttonEl, clickEvent) {
        this._el = buttonEl;
        if (this._el != null) {
            this._buttonTexts = JSON.parse((<any>this._el.dataset).wizardControlTexts);
            this._el.addEventListener('click', () => clickEvent());
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

    public updateText(stepTitle: string) {
        if (this._el != null) {
            if (this._buttonTexts.hasOwnProperty(stepTitle)) {
                this._el.innerHTML = this._buttonTexts[stepTitle];
            } else {
                this._el.innerHTML = this._buttonTexts['default'];
            }
        }
    }
}

interface Step {
    stepIcon: StepIcon;
    panel: Panel;
    title: string;
    nextButton: ControlButton;
    previousButton: ControlButton;
    finishedCallback: any;

    setNextStep(step: Step);

    setPreviousStep(step: Step);

    setActive();

    finish();

    goToNext(): Step;

    goToPrevious(): Step;
}

class StepImpl implements Step {
    stepIcon: StepIcon;
    panel: Panel;
    title: string;
    nextButton: ControlButton;
    previousButton: ControlButton;
    finishedCallback: any;

    private _nextStep: Step = null;
    private _previousStep: Step = null;

    constructor(stepIcon, panel, buttonNext, buttonPrevious, finishedCallback = null) {
        this.stepIcon = stepIcon;
        this.panel = panel;
        this.title = stepIcon.getTitle();
        this.nextButton = buttonNext;
        this.previousButton = buttonPrevious;
        this.finishedCallback = finishedCallback;
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
        this.finishedCallback();
    }

    public goToNext(): Step {
        if (this.validate()) {
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

    public goToPrevious(): Step {
        if (this._previousStep != null) {
            this._previousStep.setActive();
            return this._previousStep;
        } else {
            return this;
        }
    }

    protected validate() {
        let isValid: boolean = true;
        let panelEl = this.panel.getEl();
        let formElements: HTMLElement[] = Array.from(panelEl.querySelectorAll('[data-form-validation]'));
        if (formElements.length > 0) {
            formElements.forEach((fieldElement: HTMLElement) => {
                isValid = Utils.validateHTMLElement(fieldElement) && isValid;
            });
        }

        this.panel.updateHeight();
        return isValid;
    }

    private _updateButtons() {
        this.previousButton.updateText(this.title);
        this.nextButton.updateText(this.title);
        this._previousStep == null ? this.previousButton.hide() : this.previousButton.show();
    }
}

class StepImplGithub extends StepImpl implements Step {
    protected validate(): boolean {
        let isValid: boolean = true;
        let panelEl = this.panel.getEl();
        let formElements: HTMLElement[] = Array.from(panelEl.querySelectorAll('[data-form-validation]'));
        if (formElements.length > 0) {
            formElements.forEach((fieldElement: HTMLElement) => {
                if (fieldElement.dataset.formValidation == 'github') {
                    isValid = Utils.validateHTMLElement(fieldElement, (githubLink: string) => {
                        Github.getGithubInfo(githubLink).then((info: GithubIssue) => {
                            console.log(info);
                            let title = panelEl.parentElement.parentElement.querySelector('[data-wizard-title]');
                            if (title) {
                                title.innerHTML = `${info.title} <span class="text-muted">#${info.number}</span>`;
                            }
                        });
                    }) && isValid;
                } else {
                    isValid = Utils.validateHTMLElement(fieldElement) && isValid;
                }
            });
        }

        this.panel.updateHeight();
        return isValid;
    }
}

class StepIcon {
    private _el: HTMLElement;
    private _title: string;

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

    constructor(panelEl) {
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
    private _el: HTMLElement;
    private _buttonNext: ControlButton;
    private _buttonPrevious: ControlButton;
    private readonly _stepsEl: HTMLElement[];
    private readonly _panelsEl: HTMLElement[];
    private _steps: Step[];

    public currentStep: Step;
    public numberOfSteps: number;

    constructor(wizardEl: HTMLElement, buttonNext: HTMLElement, buttonPrevious: HTMLElement, finishedCallback) {
        this._el = wizardEl;
        this._stepsEl = Array.from(this._el.querySelectorAll('[data-wizard-step'));
        this._panelsEl = Array.from(this._el.querySelectorAll('[data-wizard-panel]'));
        this.numberOfSteps = this._stepsEl.length;
        this._initButtons(buttonNext, buttonPrevious);
        this._initSteps(finishedCallback);
    }

    public goToNextStep() {
        this.currentStep = this.currentStep.goToNext();
    }

    public goToPreviousStep() {
        this.currentStep = this.currentStep.goToPrevious();
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
            let panel: Panel = new Panel(this._panelsEl[i]);
            let step: Step;

            switch ((<any>stepIcon.getEl().dataset).wizardStep) {
                case 'github':
                    step = new StepImplGithub(stepIcon, panel, this._buttonNext, this._buttonPrevious);
                    break;
                default:
                    step = new StepImpl(stepIcon, panel, this._buttonNext, this._buttonPrevious, +1 == this.numberOfSteps ? finishedCallback : null);
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
        this._buttonNext = new ControlButton(buttonNext, () => {this.goToNextStep(); });
        this._buttonPrevious = new ControlButton(buttonPrevious, () => {this.goToPreviousStep(); });
    }

    /*public goToStep(stepNumber: number) {
        this.steps.forEach((step) => {
            if (step.number < stepNumber) {
                step.stepIcon.setCompleted();
            } else {
                step.stepIcon.unsetCompleted();
            }
        })
    }*/
}

export class Wizard {
    private readonly _el: HTMLElement;
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

Utils.loadOnPageReady(() => {
    Array.from(document.querySelectorAll('[data-wizard="true"]')).forEach((wizardEl) => {
        new Wizard(wizardEl, () => alert('initate fund transaction'));
    });
});
