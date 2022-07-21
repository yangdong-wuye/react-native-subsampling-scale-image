import type { ImageSourcePropType, StyleProp, ViewStyle } from 'react-native';

export enum MinimumScaleType {
  SCALE_TYPE_CENTER_INSIDE = 1,
  SCALE_TYPE_CENTER_CROP = 2,
  SCALE_TYPE_CUSTOM = 3,
  SCALE_TYPE_START = 4,
}

export enum ScaleType {
  FIT_CENTER,
  CENTER,
  CENTER_INSIDE,
  CENTER_CROP,
  FIT_START,
  FIT_END,
  FIT_XY,
}

export enum DoubleTapZoomStyle {
  ZOOM_FOCUS_FIXED = 1,
  ZOOM_FOCUS_CENTER = 2,
  ZOOM_FOCUS_CENTER_IMMEDIATE = 3,
}

export interface OnLoadEvent {
  nativeEvent: {
    width: number;
    height: number;
  };
}

export interface OnScaleChangedEvent {
  nativeEvent: {
    newScale: number;
    origin: number;
  };
}

export interface OnCenterChangedEvent {
  nativeEvent: {
    newCenter: { x: number; y: number };
    origin: number;
  };
}

export interface SubsamplingScaleImageProps {
  style?: StyleProp<ViewStyle>;
  source: ImageSourcePropType;
  quickScaleEnabled?: boolean;
  zoomEnabled?: boolean;
  panEnabled?: boolean;
  eagerLoadingEnabled?: boolean;
  minScale?: number;
  maxScale?: number;
  animateScale?: number;
  animateCenter?: { x: number; y: number };
  animateScaleAndCenter?: {
    animateScale: number;
    animateCenter: { x: number; y: number };
  };
  doubleTapZoomDpi?: number;
  doubleTapZoomDuration?: number;
  doubleTapZoomStyle?: DoubleTapZoomStyle;
  minimumScaleType?: MinimumScaleType;
  onLoadStart?: () => void;
  onError?: () => void;
  onLoad?: (event: OnLoadEvent) => void;
  onLoadEnd?: () => void;
  onLoadCleared?: () => void;
  onScaleChanged?: (event: OnScaleChangedEvent) => void;
  onCenterChanged?: (event: OnCenterChangedEvent) => void;
  children?: React.ReactNode;
}

export interface ScaleImageProps {
  style?: StyleProp<ViewStyle>;
  source: ImageSourcePropType;
  quickScaleEnabled?: boolean;
  zoomEnabled?: boolean;
  minScale?: number;
  maxScale?: number;
  minimumScaleType?: MinimumScaleType; // only long image
  scaleType?: ScaleType; // only simple image
  onLoadStart?: () => void;
  onError?: () => void;
  onLoad?: (event: OnLoadEvent) => void;
  onLoadEnd?: () => void;
  onLoadCleared?: () => void;
  children?: React.ReactNode;
}

export interface ImageProps {
  style?: StyleProp<ViewStyle>;
  source: ImageSourcePropType;
  minimumScaleType?: MinimumScaleType; // only long image
  scaleType?: ScaleType; // only simple image
  onLoadStart?: () => void;
  onError?: () => void;
  onLoad?: (event: OnLoadEvent) => void;
  onLoadEnd?: () => void;
  onLoadCleared?: () => void;
  children?: React.ReactNode;
}
