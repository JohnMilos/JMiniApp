import type {ReactNode} from 'react';
import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

type FeatureItem = {
  title: string;
  Svg: React.ComponentType<React.ComponentProps<'svg'>>;
  description: ReactNode;
};

const FeatureList: FeatureItem[] = [
  {
    title: 'Educational & Simple',
    Svg: require('@site/static/img/undraw_docusaurus_mountain.svg').default,
    description: (
      <>
        Learn Java architecture through hands-on practice. Extend <code>JMiniApp</code>,
        implement three lifecycle methods, and understand design patterns. Perfect for
        students and developers learning application development.
      </>
    ),
  },
  {
    title: 'Seamless State Persistence',
    Svg: require('@site/static/img/undraw_docusaurus_tree.svg').default,
    description: (
      <>
        File-based storage with automatic persistence across sessions. Import and
        export data in multiple formats (JSON, CSV, and more). Build standalone
        applications without database complexity or external dependencies.
      </>
    ),
  },
  {
    title: 'Extensible & Flexible',
    Svg: require('@site/static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
        Create custom format adapters, implement merge strategies, and extend
        the framework to fit your needs. Clean abstractions and pluggable components
        teach solid software design principles.
      </>
    ),
  },
];

function Feature({title, Svg, description}: FeatureItem) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} role="img" />
      </div>
      <div className="text--center padding-horiz--md">
        <Heading as="h3">{title}</Heading>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures(): ReactNode {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
