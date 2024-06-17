import JohnSmithDoctor from "@/assets/stomatolog-home-page-placeholder.jpg";
import EmilyJohnsonDoctor from "@/assets/Emily-doctor.avif";
import MichaelLeeDoctor from "@/assets/michaelLee-doctor.avif";
export interface ShowoffDoctor {
  name: string;
  image: string;
  description: string;
  rating: number;
}

export const doctorsToShow: ShowoffDoctor[] = [
  {
    name: "Dr. John Smith",
    image: JohnSmithDoctor,
    description:
      "With over 15 years of experience in cosmetic and restorative dentistry, Dr. Smith is known for his gentle touch and personalized care, ensuring each patient feels at ease. He is dedicated to staying updated with the latest dental advancements to provide top-notch care.",
    rating: 5,
  },
  {
    name: "Dr. Emily Johnson",
    image: EmilyJohnsonDoctor,
    description:
      "Specializing in pediatric dentistry with over a decade of experience, Dr. Johnson creates a friendly and comfortable environment for children, ensuring they have a positive dental experience. She is passionate about preventive care and patient education.",
    rating: 4.7,
  },
  {
    name: "Dr. Michael Lee",
    image: MichaelLeeDoctor,
    description:
      "Excelling in orthodontics and transforming smiles for over 12 years, Dr. Lee's expertise in braces and aligners helps patients achieve their desired results. He is known for his meticulous approach and patient-centered care.",
    rating: 4.4,
  },
];
