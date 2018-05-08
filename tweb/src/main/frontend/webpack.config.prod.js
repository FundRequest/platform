const _ = require("lodash");
const baseConfig = require("./webpack.config.base");
const webpack = require('webpack');
const {VueLoaderPlugin} = require('vue-loader');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');

let prodConfig = {
    mode: 'production',
    optimization: {
        minimizer: [
            new UglifyJSPlugin({
                uglifyOptions: {
                    output: {
                        comments: false
                    },
                    sourceMap: true,
                    compress: {
                        booleans: true
                    }
                }
            })
        ]
    },
    plugins: [
        new VueLoaderPlugin(),
        new webpack.LoaderOptionsPlugin({
            minimize: true
        })
    ],
    devtool: '#source-map'
};

module.exports = _.extend({}, baseConfig, prodConfig);
