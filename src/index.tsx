import React, { Fragment } from 'react';
import {
  Image,
  Platform,
  requireNativeComponent,
  StyleSheet,
  View,
} from 'react-native';
import type {
  OnCenterChangedEvent,
  OnLoadEvent,
  OnScaleChangedEvent,
  SubsamplingScaleImageProps,
} from './types';

const SubsamplingScaleImageView: any =
  Platform.OS === 'android'
    ? requireNativeComponent('SubsamplingScaleImageView')
    : null;

export const SubsamplingScaleImage = React.forwardRef(
  (props: SubsamplingScaleImageProps, ref: any) => {
    const {
      style,
      source,
      onLoadStart,
      onLoad,
      onLoadEnd,
      onError,
      onLoadCleared,
      onScaleChanged,
      onCenterChanged,
      children,
      ...otherProps
    } = props;

    const _onLoadStart = () => !!onLoadStart && onLoadStart();
    const _onError = () => !!onError && onError();
    const _onLoad = (event: OnLoadEvent) => !!onLoad && onLoad(event);
    const _onLoadEnd = () => !!onLoadEnd && onLoadEnd();
    const _onLoadCleared = () => !!onLoadCleared && onLoadCleared();
    const _onScaleChanged = (event: OnScaleChangedEvent) =>
      !!onScaleChanged && onScaleChanged(event);
    const _onCenterChanged = (event: OnCenterChangedEvent) =>
      !!onCenterChanged && onCenterChanged(event);

    const resolveAssetSource = Image.resolveAssetSource(source);

    return (
      <View ref={ref} style={[styles.imageContainer, style]}>
        <Fragment>
          <SubsamplingScaleImageView
            style={StyleSheet.absoluteFill}
            source={resolveAssetSource}
            onSubsamplingScaleImageLoadStart={_onLoadStart}
            onSubsamplingScaleImageLoadError={_onError}
            onSubsamplingScaleImageLoad={_onLoad}
            onSubsamplingScaleImageLoadEnd={_onLoadEnd}
            onSubsamplingScaleImageLoadCleared={_onLoadCleared}
            onSubsamplingScaleImageScaleChanged={_onScaleChanged}
            onSubsamplingScaleImageCenterChanged={_onCenterChanged}
            {...otherProps}
          />
          {children}
        </Fragment>
      </View>
    );
  }
);

const styles = StyleSheet.create({
  imageContainer: {
    overflow: 'hidden',
  },
});

export * from './Image';
export * from './ScaleImage';
export * from './types';
