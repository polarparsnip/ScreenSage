import s from './About.module.scss'

export default function About() {
  document.title = 'About';

  return (
    <div className={s.about}>
      <div>
        <h1>About us</h1>
      </div>
      <div className={s.about__text_body}>
        <p>
          Lorem ipsum odor amet, consectetuer adipiscing elit. 
          Fames vel ipsum curabitur porttitor, ligula dui nec augue commodo. 
          Sollicitudin lacus commodo ac class aenean pharetra. 
          Tempus mus cubilia fermentum tempor curae dignissim interdum morbi. 
          Maximus nec sapien faucibus non magnis sollicitudin accumsan est. 
          Fringilla placerat lectus venenatis curae fermentum luctus. 
          Praesent accumsan tristique natoque magna inceptos blandit. 
          Gravida suscipit mattis convallis eu efficitur sit natoque. 
          Phasellus curabitur id, elementum faucibus facilisi ipsum. 
          Ante lacus ultrices eleifend a amet neque tempus mus varius.
        </p>
        <p>
          Lorem ipsum odor amet, consectetuer adipiscing elit. 
          Fames vel ipsum curabitur porttitor, ligula dui nec augue commodo. 
          Sollicitudin lacus commodo ac class aenean pharetra. 
          Tempus mus cubilia fermentum tempor curae dignissim interdum morbi. 
          Maximus nec sapien faucibus non magnis sollicitudin accumsan est. 
          Fringilla placerat lectus venenatis curae fermentum luctus. 
          Praesent accumsan tristique natoque magna inceptos blandit. 
          Gravida suscipit mattis convallis eu efficitur sit natoque. 
          Phasellus curabitur id, elementum faucibus facilisi ipsum. 
          Ante lacus ultrices eleifend a amet neque tempus mus varius.
        </p>
      </div>
    </div>
  )
};