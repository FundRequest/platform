// jshint ignore: start
// fetch command line arguments
let dev = false;

const arg = (argList => {
    let arg = {}, a, opt, thisOpt, curOpt;
    for (a = 0; a < argList.length; a++) {

        thisOpt = argList[a].trim();
        opt = thisOpt.replace(/^\-+/, '');

        if (opt === thisOpt) {

            // argument value
            if (curOpt) arg[curOpt] = opt;
            curOpt = null;

        }
        else {

            // argument name
            curOpt = opt;
            arg[curOpt] = true;
        }
    }
    return arg;
})(process.argv);


const gulp = require('gulp'),
    run = require('gulp-run'),
    sass = require('gulp-sass'),
    install = require("gulp-install"),
    gulpif = require('gulp-if'),
    rename = require('gulp-rename'),
    plumber = require('gulp-plumber'),
    notify = require('gulp-notify'),
    sassLint = require('gulp-sass-lint'),
    sourcemaps = require('gulp-sourcemaps'),
    tildeImporter = require('node-sass-tilde-importer'),
    runSequence = require('run-sequence'),
    amdOptimize = require('amd-optimize'),
    postcss = require('gulp-postcss'),
    autoprefixer = require('autoprefixer'),
    cssnano = require('cssnano'),
    ts = require('gulp-typescript');

let target = "../../../target/classes/static/assets";
let origin = "./src";

let displayError = function(error) {
    // Initial building up of the error
    var errorString = '[' + error.plugin.error.bold + ']';
    errorString += ' ' + error.message.replace("\n", ''); // Removes new line at the end

    // If the error contains the filename or line number add it to the string
    if (error.fileName)
        errorString += ' in ' + error.fileName;

    if (error.lineNumber)
        errorString += ' on line ' + error.lineNumber.bold;

    // This will output an error like the following:
    // [gulp-sass] error message in file_name on line 1
    console.error(errorString);
};

let onError = function(err) {
    notify.onError({
        title: "Gulp",
        subtitle: "Failure!",
        message: "Error: <%= error.message %>",
        sound: "Basso"
    })(err);
    this.emit('end');
};

let sassOptions = {
    outputStyle: 'expanded',
    importer: tildeImporter
};

let autoprefixerConfig = {
    browsers: ['last 3 versions'],
    remove: false
};
let cssnanoConfig = {
    reduceIdents: false
};

function runSass(filename, cssTarget) {
    let plugins = [
        autoprefixer(autoprefixerConfig),
        cssnano(cssnanoConfig)
    ];

    return gulp.src(filename)
        .pipe(plumber({errorHandler: onError}))
        .pipe(sass(sassOptions))
        .pipe(postcss([plugins[0]]))
        .pipe(gulp.dest(cssTarget))
        .pipe(postcss(plugins))
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest(cssTarget));
}

function runTs(tsConfig, filename) {
    let tsProject = ts.createProject(tsConfig);
    let tsResult = gulp.src(filename)
        .pipe(tsProject());

    return tsResult.pipe(gulp.dest(`${target}/js`));
}

gulp.task('scripts', function() {
    return runTs('tsconfig.json', [`${origin}/js/**/*.ts`, `!${origin}/js/vue/**/*`]);
});

gulp.task('typechain', function() {
    return run("npm run typechain").exec();
});

gulp.task('copy-dependencies', function() {
    let copy = [];
    copy.push(gulp.src(['node_modules/jquery/dist/*']).pipe(gulp.dest(`${target}/vendors/jquery`)));
    copy.push(gulp.src(['node_modules/bootstrap/dist/**/*']).pipe(gulp.dest(`${target}/vendors/bootstrap`)));
    copy.push(gulp.src(['node_modules/mdbootstrap/js/mdb.*']).pipe(gulp.dest(`${target}/vendors/mdbootstrap/js`)));
    copy.push(gulp.src(['node_modules/mdbootstrap/font/**/*']).pipe(gulp.dest(`${target}/vendors/mdbootstrap/font`)));
    copy.push(gulp.src(['node_modules/mdbootstrap/img/**/*']).pipe(gulp.dest(`${target}/vendors/mdbootstrap/img`)));
    copy.push(gulp.src(['node_modules/clipboard/dist/**/*']).pipe(gulp.dest(`${target}/vendors/clipboard/js`)));
    copy.push(gulp.src(['node_modules/headroom.js/dist/headroom.*']).pipe(gulp.dest(`${target}/vendors/headroom.js`)));
    copy.push(gulp.src(['node_modules/node-waves/dist/*']).pipe(gulp.dest(`${target}/vendors/node-waves`)));
    copy.push(gulp.src(['node_modules/datatables.net*/**/*.+(css|js)']).pipe(gulp.dest(`${target}/vendors`)));
    copy.push(gulp.src(['node_modules/requirejs/*.js']).pipe(gulp.dest(`${target}/vendors/requirejs`)));
    copy.push(gulp.src(['node_modules/lightslider/dist/**/*']).pipe(gulp.dest(`${target}/vendors/lightslider`)));
    copy.push(gulp.src(['node_modules/typeface-titillium-web/index.css']).pipe(gulp.dest(`${target}/vendors/typeface-titillium-web`)));
    copy.push(gulp.src(['node_modules/typeface-titillium-web/files/*']).pipe(gulp.dest(`${target}/vendors/typeface-titillium-web/files`)));
    copy.push(gulp.src(['node_modules/web3/dist/*']).pipe(gulp.dest(`${target}/vendors/web3`)));
    return copy;
});

gulp.task('copy-assets', function() {
    let copy = [];
    copy.push(gulp.src([`${origin}/fonts/**/*`]).pipe(gulp.dest(`${target}/fonts`)));
    copy.push(gulp.src([`${origin}/img/**/*`]).pipe(gulp.dest(`${target}/img`)));
    copy.push(gulp.src([`${origin}/js/**/*.js`, `!${origin}/js/vue/**/*`]).pipe(gulp.dest(`${target}/js`)));

    return copy;
});

gulp.task('styles-bootstrap', function() {
    return runSass(`${origin}/scss/bootstrap.scss`, `${target}/vendors/bootstrap/css`)
});

gulp.task('styles-mdb', function() {
    return runSass(`${origin}/scss/mdb.scss`, `${target}/vendors/mdbootstrap/css`)
});

gulp.task('styles-core', function() {
    return runSass(`${origin}/scss/core.scss`, `${target}/css`)
});

gulp.task('styles-website', function() {
    return runSass(`${origin}/scss/website.scss`, `${target}/css`);
});

gulp.task('install', function() {
    return gulp.src(["./package.json"]).pipe(install());
});

gulp.task('run-watch', function() {
    gulp.watch([`${origin}/js/*.js`], ['copy-assets']);
    gulp.watch([`${origin}/scss/bootstrap.scss`, `${origin}/scss/fundrequest/+(_variables|_colors).scss`], ['styles-bootstrap']);
    gulp.watch([`${origin}/scss/mdb.scss`, `${origin}/scss/mdb-overrides/*.scss`, `${origin}/scss/fundrequest/+(_variables|_colors).scss`], ['styles-mdb']);
    gulp.watch([`${origin}/scss/core.scss`, `${origin}/scss/fundrequest/**/*.scss`, `!${origin}/scss/fundrequest/website/*.scss`], ['styles-core']);
    gulp.watch([`${origin}/scss/website.scss`, `${origin}/scss/fundrequest/website/*.scss`], ['styles-website']);
    gulp.watch([`${origin}/js/app/*.ts`], ['scripts']);
});

gulp.task('default', function(done) {
    target = (arg && arg.target) || target;
    runSequence('copy-dependencies', 'copy-assets', 'styles-bootstrap', 'styles-mdb', 'styles-core', 'styles-website',  'scripts', 'typechain', done);
});

gulp.task('watch', function(done) {
    target = (arg && arg.target) || target;
    runSequence('copy-dependencies', 'copy-assets', 'styles-bootstrap', 'styles-mdb', 'styles-core', 'styles-website', 'scripts', 'run-watch', done);
});
